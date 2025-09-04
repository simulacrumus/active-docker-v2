from datetime import datetime
from datetime import timedelta
import json
import re
import requests
from bs4 import BeautifulSoup
import calendar
from playwright.async_api import async_playwright
import random
import asyncio
from urllib.parse import urlparse
import os

def get_json_data(file):
    with open(file) as json_file:
        return json.load(json_file)

def write_json_data(data, filename):
    with open(filename, 'w') as outfile:
        json.dump(data, outfile)

def string_to_num(str:str):
    return int(filter(str.isdigit, str))

def remove_duplicates_from_list(list:list):
    return [*set(list)]

def get_next_weekday(date, weekday):
    days_ahead = weekday - date.weekday()
    if days_ahead < 0:
        days_ahead = days_ahead + 7
    return date + timedelta(days=days_ahead)

async def get_soup_for_url(url:str):
    async with async_playwright() as p:
        random_user_agents = [
            'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36',
            'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36',
            'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36'
        ]
        user_agent = random.choice(random_user_agents)

        viewport_width = random.randint(1280, 1920)
        viewport_height = random.randint(800, 1200)

        headless_env = True
        browser = await p.chromium.launch_persistent_context(
            user_data_dir="./browser_data",
            headless=headless_env,
            args=[
                '--disable-blink-features=AutomationControlled',
                '--no-sandbox',
                '--disable-dev-shm-usage'
            ],
            viewport={"width": viewport_width, "height": viewport_height},
            user_agent=user_agent,
            locale='en-US',
            timezone_id='America/New_York'
        )

        page = await browser.new_page()

        # Stealth-like evasions
        await page.add_init_script(
            """
            Object.defineProperty(navigator, 'webdriver', {get: () => undefined});
            window.chrome = { runtime: {} };
            Object.defineProperty(navigator, 'languages', {get: () => ['en-US', 'en']});
            Object.defineProperty(navigator, 'plugins', {get: () => [1, 2, 3]});
            Object.defineProperty(navigator, 'hardwareConcurrency', {get: () => 8});
            Object.defineProperty(navigator, 'deviceMemory', {get: () => 8});
            const originalQuery = window.navigator.permissions && window.navigator.permissions.query;
            if (originalQuery) {
              window.navigator.permissions.query = (parameters) => (
                parameters && parameters.name === 'notifications' ?
                  Promise.resolve({ state: Notification.permission }) :
                  originalQuery(parameters)
              );
            }
            """
        )

        await page.set_extra_http_headers({
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
            'Accept-Language': 'en-US,en;q=0.9',
            'DNT': '1',
            'Connection': 'keep-alive',
            'Upgrade-Insecure-Requests': '1'
        })

        async def load_with_retries(target_url:str, attempts:int=5):
            last_html = ''
            for attempt in range(1, attempts+1):
                # Warm up domain cookies by visiting origin first
                try:
                    parsed = urlparse(target_url)
                    origin = f"{parsed.scheme}://{parsed.netloc}"
                    await page.goto(origin, wait_until='domcontentloaded')
                    await page.wait_for_load_state('networkidle')
                    await asyncio.sleep(random.uniform(1.0, 2.0))
                except Exception:
                    pass

                await page.goto(target_url, wait_until='domcontentloaded')
                await page.wait_for_load_state('networkidle')

                # Simulate human behavior
                try:
                    await page.mouse.move(random.randint(0, viewport_width), random.randint(0, viewport_height), steps=10)
                    await page.mouse.wheel(delta_x=0, delta_y=random.randint(300, 800))
                except Exception:
                    pass

                # Allow any JS challenge to run
                await asyncio.sleep(random.uniform(3.0, 6.0))
                html = await page.content()
                if 'Incapsula' in html or 'Request unsuccessful' in html:
                    # If there is an iframe challenge, wait and refresh
                    try:
                        frame = page.frame(name='main-iframe')
                        if frame:
                            await frame.wait_for_load_state(timeout=8000)
                    except Exception:
                        pass

                    await asyncio.sleep(min(12, 3 * attempt))
                    try:
                        await page.reload(wait_until='networkidle')
                        await asyncio.sleep(random.uniform(2.0, 4.0))
                        html = await page.content()
                        if 'Incapsula' not in html and 'Request unsuccessful' not in html:
                            return html
                    except Exception:
                        pass
                    last_html = html
                    continue
                return html
            return last_html

        content = await load_with_retries(url, attempts=4)
        await browser.close()
        return BeautifulSoup(content, 'html.parser')

def get_month_number(month_name):
    return datetime.strptime(month_name, '%B').month

# def contains_month_name(string):
#     month_names = list(calendar.month_name)[1:]
#     pattern = re.compile(r'\b(?:' + '|'.join(month_names) + r')\b', re.IGNORECASE)
#     return bool(pattern.search(string))