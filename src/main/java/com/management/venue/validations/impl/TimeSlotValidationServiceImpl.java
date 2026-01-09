package com.management.venue.validations.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.management.venue.logger.BaseLogger;
import com.management.venue.pojo.TimeSlotData;
import com.management.venue.pojo.ValidationData;
import com.management.venue.validations.service.GenericValidationService;

import io.micrometer.common.util.StringUtils;

@Service
public class TimeSlotValidationServiceImpl extends BaseLogger implements GenericValidationService<TimeSlotData> {

    // Standard format for sports booking systems
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<ValidationData> validate(TimeSlotData entity) {
        List<ValidationData> exceptions = new ArrayList<>();
        boolean isUpdate = StringUtils.isNotEmpty(entity.getPk());

        LocalDateTime start = null;
        LocalDateTime end = null;

        // 1. Validate Start Time
        if (StringUtils.isNotEmpty(entity.getStartTime())) {
            try {
                start = LocalDateTime.parse(entity.getStartTime(), FORMATTER);
                if (!isUpdate && start.isBefore(LocalDateTime.now())) {
                    exceptions.add(new ValidationData("startTime", "Start time cannot be in the past"));
                }
            } catch (Exception e) {
                exceptions.add(new ValidationData("startTime", "Invalid format. Use yyyy-MM-dd HH:mm:ss"));
            }
        } else if (!isUpdate) {
            exceptions.add(new ValidationData("startTime", "Start time is required"));
        }

        // 2. Validate End Time
        if (StringUtils.isNotEmpty(entity.getEndTime())) {
            try {
                end = LocalDateTime.parse(entity.getEndTime(), FORMATTER);
            } catch (Exception e) {
                exceptions.add(new ValidationData("endTime", "Invalid format. Use yyyy-MM-dd HH:mm:ss"));
            }
        } else if (!isUpdate) {
            exceptions.add(new ValidationData("endTime", "End time is required"));
        }

        // 3. Logical Range Check (Start must be before End)
        if (start != null && end != null && !start.isBefore(end)) {
            exceptions.add(new ValidationData("endTime", "End time must be after start time"));
        }

        return exceptions;
    }
}