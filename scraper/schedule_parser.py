import re
import datetime
import logging

def format_times_string(times_string:str):
    # Remove extra info that some facilities have
    times_string = times_string.replace('Arena 1','')
    # Some schedules have typos
    times_string = times_string.replace('a,','am')
    # Some schedules use to innstead of dash
    times_string = times_string.replace('to','-')
    # Weird dash, replace character U+2013 with the character U+002d
    times_string = times_string.replace("–", "-")
    # Convert Noon or noon to 12:00pm
    times_string = re.sub(r'(Noon|noon)',r'12:00pm',times_string)
    # Keep only numbers,comma, dash, am, pm and colon
    times_string = re.sub(r'[^0-9apm:,-]', '', times_string)
    # Add missing comma
    times_string = re.sub(r'([0-9:apmAPM]+-[0-9:apmAPM]+[apmAPM])', r'\1,', times_string)
    # Remove double commas
    times_string =  re.sub(r',+', ',', times_string)
    #Remove extra comma at the end
    times_string = re.sub(r',\s*$', '', times_string)
    return times_string

def create_time(am_pm, time_string):
    time = re.sub(r'[^\d]+', '', time_string)
    if len(time) == 1: time = "0"+time+"00"
    elif len(time) == 2: time = time+"00"
    elif len(time) == 3: time = "0"+time
    return datetime.datetime.strptime(time+am_pm, '%I%M%p')

def parse_schedule_string(times_string:str):
    # return an empty array if there's no time (n/a)
    if times_string.lower().__contains__('n/a'): return []
    # Clean and format times string
    times_string = format_times_string(times_string)
    # Declare output list
    result = []
    # Split string by comma for multiple start and end hours
    schedules = times_string.split(",")
    for schedule in schedules:
        try:
            start, end = schedule.split("-")
            end_am_pm = "pm"
            if end.lower().__contains__("am"): 
                end_am_pm="am" 
            start_am_pm = end_am_pm
            if start.lower().__contains__("am"): 
                start_am_pm="am" 
            elif start.lower().__contains__("pm"):
                start_am_pm="pm"
            start_time = create_time(start_am_pm, start)
            end_time = create_time(end_am_pm, end)
            # Usually start time does not have am or pm and by default set to end
            # But in case 11-1pm, 11 becomes 11 pm, so it needs to be connverted to am
            if start_time > end_time:
                start_time = start_time - datetime.timedelta(hours=12)
            result.append({"start_time":str(start_time.time()), "end_time":str(end_time.time())})
        except(RuntimeError) as e:
            logging.error(e)
    return result

# def run():
#     #  'Noon - 1:30 pm Arena 1 	',
#     times = ['7:30 - 8:30 am, 8:30 - 9:30 am, 9:30 - 10:30 am, 7:30 - 8 pm',
#              'Noon - 1 pm',
#              '9:30 - 10:30 am, 11:30 - 1 pm',
#              'n/a',
#              '2:10 - 3:10 pm 3:20 - 4:20 pm',
#              '11 -noon, Noon - 1 pm, 1 - 3 pm ',
#              '11 am - 1 pm ',
#              '1 - 3 pm',
#              '11 am - noon,  12:15 - 1:15 pm',
#              '7 - 8:30 am Arena 1   ',
#              '1:30 - 3 pm Arena 1 ',
#              '8:15 - 9:15 am, 9:15 - 10:15 am',
#              '8:15 - 9:15 am 9:15 - 10:15 am',
#              '2 - 3 pm 3 - 4 pm',
#              '11:30 am - 12:30 pm',
#              '11 a, - noon, Noon - 1pm, 7 - 8 pm,  8 - 9 pm',
#              '3:45 - 4:45 pm, 4:45 - 5:45 pm, 5:45 - 6:45 pm',
#              '7:30 - 9:30 am, 10:30 am to noon, 8 - 9 pm'
#             ]
#     for time in times:
#         t = parse_schedule_string(time)
#         print(t)
#         print()

# if __name__ == '__main__':
#     run()