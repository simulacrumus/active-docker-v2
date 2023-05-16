package com.example.active.business.controller;

import com.example.active.business.domain.TypeDTO;
import com.example.active.business.domain.enums.CityFilterEnum;
import com.example.active.business.domain.enums.LanguageFilterEnum;
import com.example.active.business.domain.enums.TypeSortEnum;
import com.example.active.business.domain.paramobject.TypeParams;
import com.example.active.business.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TypeController {

    @Autowired
    private TypeService service;

    @RequestMapping(value = "/types",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public Page<TypeDTO> searchTypes(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE) Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @RequestParam(name = "q") Optional<String> query,
            @RequestParam(name = "sort") Optional<TypeSortEnum> sort,
            @RequestParam(name = "page") Optional<Integer> pageNumber,
            @RequestParam(name = "size") Optional<Integer> pageSize,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        TypeParams params = TypeParams.builder()
                .sort(sort)
                .query(query)
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .language(language)
                .city(city);
        try{
            return service.findAll(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @RequestMapping(value = "/types/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public TypeDTO getTypeById(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE) Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @PathVariable(name = "id") Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        TypeParams params = TypeParams.builder()
                .typeId(id)
                .language(language)
                .city(city);
        try{
            return service.getById(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @RequestMapping(value = "/categories/{categoryId}/types",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public Page<TypeDTO> searchTypesByCategory(
            @PathVariable(name = "categoryId") Long categoryId,
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE) Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @RequestParam(name = "q") Optional<String> query,
            @RequestParam(name = "sort") Optional<TypeSortEnum> sort,
            @RequestParam(name = "page") Optional<Integer> pageNumber,
            @RequestParam(name = "size") Optional<Integer> pageSize,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        TypeParams params = TypeParams.builder()
                .categoryId(categoryId)
                .sort(sort)
                .query(query)
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .language(language)
                .city(city);
        try{
            return service.findAllByCategory(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @RequestMapping(value = "/facilities/{facilityId}/types",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public Page<TypeDTO> searchTypesByFacility(
            @PathVariable(name = "facilityId") Long facilityId,
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE) Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @RequestParam(name = "q") Optional<String> query,
            @RequestParam(name = "sort") Optional<TypeSortEnum> sort,
            @RequestParam(name = "page") Optional<Integer> pageNumber,
            @RequestParam(name = "size") Optional<Integer> pageSize,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        TypeParams params = TypeParams.builder()
                .facilityId(facilityId)
                .sort(sort)
                .query(query)
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .language(language)
                .city(city);
        try{
            return service.findAllByFacility(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @RequestMapping(value = "/facilities/{facilityId}/categories/{categoryId}/types",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public Page<TypeDTO> searchTypesByFacilityAndCategory(
            @PathVariable(name = "facilityId") Long facilityId,
            @PathVariable(name = "categoryId") Long categoryId,
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE) Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @RequestParam(name = "q") Optional<String> query,
            @RequestParam(name = "sort") Optional<TypeSortEnum> sort,
            @RequestParam(name = "page") Optional<Integer> pageNumber,
            @RequestParam(name = "size") Optional<Integer> pageSize,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        TypeParams params = TypeParams.builder()
                .facilityId(facilityId)
                .categoryId(categoryId)
                .sort(sort)
                .query(query)
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .language(language)
                .city(city);
        try{
            return service.findAllByFacilityAndCategory(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }
}
