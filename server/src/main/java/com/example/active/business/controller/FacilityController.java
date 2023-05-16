package com.example.active.business.controller;

import com.example.active.business.domain.FacilityDTO;
import com.example.active.business.domain.enums.ActivitySortEnum;
import com.example.active.business.domain.enums.CityFilterEnum;
import com.example.active.business.domain.enums.FacilitySortEnum;
import com.example.active.business.domain.enums.LanguageFilterEnum;
import com.example.active.business.domain.paramobject.FacilityParams;
import com.example.active.business.service.FacilityService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.NoSuchElementException;
import java.util.Optional;

@Validated
@RestController
@RequestMapping(value = "/{city}")
public class FacilityController {

    @Autowired
    @Qualifier("facilityService")
    private FacilityService service;

    @RequestMapping(value = "/facilities",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    public Page<FacilityDTO> findAll(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE)
            @Parameter(name = "language", description = "Language", example = "en") Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @RequestParam(name = "q") Optional<String> query,
            @RequestParam(name = "sort") Optional<FacilitySortEnum> sort,
            @RequestParam(name = "page") Optional<Integer> pageNumber,
            @RequestParam(name = "size") Optional<Integer> pageSize,
            @RequestParam(name = "lat") Optional<Double> latitude,
            @RequestParam(name = "lng") Optional<Double>  longitude,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        FacilityParams params = FacilityParams.builder()
                .city(city)
                .latitude(latitude)
                .longitude(longitude)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sort(sort)
                .query(query)
                .language(language);
        try{
            return service.findAll(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @RequestMapping(value = "/facilities/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public FacilityDTO findById(
            @PathVariable(name = "city") CityFilterEnum city,
            @PathVariable("id") Long id,
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE)
            @Parameter(name = "language", description = "Language", example = "en") Optional<LanguageFilterEnum> language,
            @RequestParam(name = "lat") Optional<Double> latitude,
            @RequestParam(name = "lng") Optional<Double>  longitude,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        FacilityParams params = FacilityParams.builder()
                .facilityId(id)
                .language(language)
                .latitude(latitude)
                .longitude(longitude)
                .city(city);
        try{
            return service.findById(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @RequestMapping(value = "/types/{typeId}/facilities",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public Page<FacilityDTO> findByType(
            @PathVariable("typeId") Long typeId,
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE)
            @Parameter(name = "language", description = "Language", example = "en") Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @RequestParam(name = "q") Optional<String> query,
            @RequestParam(name = "sort") Optional<FacilitySortEnum> sort,
            @RequestParam(name = "page") Optional<Integer> pageNumber,
            @RequestParam(name = "size") Optional<Integer> pageSize,
            @RequestParam(name = "lat") Optional<Double> latitude,
            @RequestParam(name = "lng") Optional<Double>  longitude,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        FacilityParams params = FacilityParams.builder()
                .city(city)
                .latitude(latitude)
                .longitude(longitude)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sort(sort)
                .query(query)
                .language(language)
                .typeId(typeId);
        try{
            return service.findAllByType(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }
}
