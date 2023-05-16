package com.example.active.business.controller;

import com.example.active.business.domain.CategoryDTO;
import com.example.active.business.domain.enums.CityFilterEnum;
import com.example.active.business.domain.enums.LanguageFilterEnum;
import com.example.active.business.domain.paramobject.CategoryParams;
import com.example.active.business.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import java.util.Optional;

@Validated
@RestController
@RequestMapping(value = "/{city}")
public class CategoryController {

    @Autowired
    @Qualifier("categoryService")
    private CategoryService service;

    @RequestMapping(value = "/categories",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public Page<CategoryDTO> searchCategories(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE) Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @RequestParam(name = "q") Optional<String> query,
            @RequestParam(name = "page") Optional<Integer> pageNumber,
            @RequestParam(name = "size") Optional<Integer> pageSize,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        CategoryParams params = CategoryParams.builder()
                .language(language)
                .city(city)
                .query(query)
                .pageNumber(pageNumber)
                .pageSize(pageSize);
        try{
            return service.findAll(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @RequestMapping(value = "/categories/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO getCategoryById(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE) Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @PathVariable(name = "id") Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        CategoryParams params = CategoryParams.builder()
                .categoryId(id)
                .language(language)
                .city(city);
        try{
            return service.findById(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @RequestMapping(value = "/facilities/{facilityId}/categories",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public Page<CategoryDTO> getCategoriesByFacility(
            @PathVariable(name = "facilityId") Long facilityId,
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE) Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @RequestParam(name = "q") Optional<String> query,
            @RequestParam(name = "page") Optional<Integer> pageNumber,
            @RequestParam(name = "size") Optional<Integer> pageSize,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        CategoryParams params = CategoryParams.builder()
                .facilityId(facilityId)
                .language(language)
                .city(city)
                .query(query)
                .pageNumber(pageNumber)
                .pageSize(pageSize);
        try{
            return service.findAllByFacility(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

}
