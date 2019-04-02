package com.soprasteria.fitbit.scheduler;

import com.soprasteria.fitbit.model.ActivitySteps;
import com.soprasteria.fitbit.model.ActivityStepsDetail;
import com.soprasteria.fitbit.model.User;
import com.soprasteria.fitbit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Iterator;


@Component
public class UpdateFitbitActivity {
    private static final Logger logger = LoggerFactory.getLogger(UpdateFitbitActivity.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private UserService userService;

    private long sumSteps(ActivitySteps activitySteps) {
        long currentSteps = 0;
        Iterator<ActivityStepsDetail> list = activitySteps.getActivitySteps().iterator();
        while (list.hasNext()) {
            ActivityStepsDetail detail = list.next();
            currentSteps += detail.getSteps();
        }
        return currentSteps;
    }


    @Scheduled(cron = "${fitbit.scheduler.cron}")
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void update() {
        logger.info("Update Fitbiy Activities :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );

        Page<User> users = userService.findAll(PageRequest.of(0, Integer.MAX_VALUE));

        Iterator itUser = users.iterator();

        while (itUser.hasNext()) {
            try {
                User user = (User) itUser.next();

                logger.debug("Update Fitbiy Activities :: Procesing :: User - {}", user.getUserId());

                LocalDate previousSunday = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
                LocalDate currentSunday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

                ActivitySteps activitiesStepsPreviousWeek = userService.getActivitiesStepsWeekBaseDate(user.getUserId(), previousSunday.toString());
                ActivitySteps activitiesStepsCurrentWeek = userService.getActivitiesStepsWeekBaseDate(user.getUserId(), currentSunday.toString());

                long previousSteps = sumSteps(activitiesStepsPreviousWeek);
                long currentSteps = sumSteps(activitiesStepsCurrentWeek);

                user.setPreviusSteps(previousSteps);
                user.setCurrentSteps(currentSteps);

                userService.save(user);

                logger.debug("Update Fitbiy Activities :: Saved :: User - {}", user.getUserId());
            } catch (Exception e) {
                logger.error(e.getMessage(), e.getStackTrace());
            }
        }

    }


}
