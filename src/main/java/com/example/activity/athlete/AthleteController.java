package com.example.activity.athlete;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AthleteController {

	private static final Log logger = LogFactory.getLog(AthleteController.class);


	@QueryMapping
	public Athlete athlete(@Argument Long id) {
		logger.debug("Getting athlete " + id);
		return AthleteSource.getAthlete(id);
	}

	@BatchMapping
	public List<List<Activity>> activities(List<Athlete> athletes) {
		logger.debug("Getting activities for " + athletes);
		return AthleteSource.getActivities(athletes);
	}

	@BatchMapping
	public List<List<Comment>> comments(List<Activity> activities) {
		logger.debug("Getting comments for " + activities);
		return AthleteSource.getComments(activities);
	}

}
