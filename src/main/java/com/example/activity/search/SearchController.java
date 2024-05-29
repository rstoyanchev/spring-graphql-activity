package com.example.activity.search;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SearchController {

	private static final Log logger = LogFactory.getLog(SearchController.class);

	@QueryMapping
	List<Object> search(@Argument String text) {
		logger.debug("Searching for '" + text + "'");
		return SearchSource.search(text);
	}

}
