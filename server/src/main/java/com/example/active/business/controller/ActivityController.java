package com.example.active.business.controller;

import com.example.active.business.domain.ActivityDTO;
import com.example.active.business.domain.enums.ActivitySortEnum;
import com.example.active.business.domain.enums.CityFilterEnum;
import com.example.active.business.domain.enums.LanguageFilterEnum;
import com.example.active.business.domain.paramobject.ActivityParams;
import com.example.active.business.service.ActivityService;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Validated
@RestController
@RequestMapping(value = "/{city}")

@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Not found - The activity was not found"),
        @ApiResponse(responseCode = "401", description = "Bad Request")
})
@ApiOperation(value = "Activities", nickname = "Activity")
public class ActivityController {
    @Autowired
    @Qualifier("activityService")
    private ActivityService service;

    @GetMapping(value = "/activities",
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Search Activities")
    public Page<ActivityDTO> searchActivities(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE)
            @Parameter(name = "language", description = "Language", example = "en") Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @RequestParam(name = "q") Optional<String> query,
            @RequestParam(name = "sort") Optional<ActivitySortEnum> sort,
            @RequestParam(name = "page") Optional<Integer> pageNumber,
            @RequestParam(name = "size") Optional<Integer> pageSize,
            @RequestParam(name = "available") Optional<Boolean> isAvailable,
            @RequestParam(name = "lat") Optional<Double> latitude,
            @RequestParam(name = "lng") Optional<Double>  longitude,
            @RequestParam(name = "time")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> time,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        ActivityParams params = ActivityParams.builder()
                .language(language)
                .city(city)
                .query(query)
                .sort(sort)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .isAvailable(isAvailable)
                .latitude(latitude)
                .longitude(longitude)
                .time(time);
        try{
            return service.findAll(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @RequestMapping(
            value = "/activities/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    public ActivityDTO getActivityById(
            @PathVariable("id") Long id,
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE)
            @Parameter(name = "language", description = "Language", example = "en") Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @RequestParam(name = "lat") Optional<Double> latitude,
            @RequestParam(name = "lng") Optional<Double>  longitude,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        ActivityParams params = ActivityParams.builder()
                .activityId(id)
                .language(language)
                .city(city)
                .latitude(latitude)
                .longitude(longitude);
        try{
            return service.findById(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @RequestMapping(
            value = "/types/{typeId}/activities",
            method = RequestMethod.GET,
            produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    public Page<ActivityDTO> searchActivitiesByType(
            @PathVariable("typeId")  @Parameter(name = "typeId", description = "Id of the activity type", example = "78") Long typeId,
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE)
            @Parameter(name = "language", description = "Language", example = "en") Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @RequestParam(name = "q") Optional<String> query,
            @RequestParam(name = "sort") Optional<ActivitySortEnum> sort,
            @RequestParam(name = "page")Optional<Integer> pageNumber,
            @RequestParam(name = "size") Optional<Integer> pageSize,
            @RequestParam(name = "available") Optional<Boolean> isAvailable,
            @RequestParam(name = "lat") Optional<Double> latitude,
            @RequestParam(name = "lng") Optional<Double>  longitude,
            @RequestParam(name = "time")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> time,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        ActivityParams params = ActivityParams.builder()
                .typeId(typeId)
                .language(language)
                .city(city)
                .query(query)
                .sort(sort)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .isAvailable(isAvailable)
                .latitude(latitude)
                .longitude(longitude)
                .time(time);

        try{
            return service.findByType(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @RequestMapping(
            value = "/facilities/{facilityId}/types/{typeId}/activities",
            method = RequestMethod.GET,
            produces = {"application/json"}
    )
    @ResponseStatus(HttpStatus.OK)
    public Page<ActivityDTO> searchActivitiesByTypeAndFacility(
            @PathVariable("typeId") Long typeId,
            @PathVariable("facilityId") Long facilityId,
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE)
            @Parameter(name = "language", description = "Language", example = "en") Optional<LanguageFilterEnum> language,
            @PathVariable(name = "city") CityFilterEnum city,
            @RequestParam(name = "q") Optional<String> query,
            @RequestParam(name = "sort") Optional<ActivitySortEnum> sort,
            @RequestParam(name = "page") Optional<Integer> pageNumber,
            @RequestParam(name = "size") Optional<Integer> pageSize,
            @RequestParam(name = "available") Optional<Boolean> isAvailable,
            @RequestParam(name = "lat") Optional<Double> latitude,
            @RequestParam(name = "lng") Optional<Double>  longitude,
            @RequestParam(name = "time")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> time,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        ActivityParams params = ActivityParams.builder()
                .facilityId(facilityId)
                .typeId(typeId)
                .language(language)
                .city(city)
                .query(query)
                .sort(sort)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .isAvailable(isAvailable)
                .latitude(latitude)
                .longitude(longitude)
                .time(time);
        try{
            return service.findByFacilityAndType(params);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }
}