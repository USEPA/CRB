package com.epa.erb.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.epa.erb.App;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectCreationControllerTest {
	private ProjectCreationController projectCreationController;
	
	@BeforeEach
	public void setUp() throws Exception {
		projectCreationController = new ProjectCreationController(new App());
	}

	@Test
	public void testInvalidProjectName() {
		assertFalse(projectCreationController.isValidNewProjectName(null));
		assertFalse(projectCreationController.isValidNewProjectName(""));
	}
	
	/*
	 * @Test public void testValidNewProjectName() { //
	 * assertFalse(projectCreationController.isValidNewProjectName(""));
	 * assertTrue(projectCreationController.
	 * isValidNewProjectName("A whole new project")); }
	 */

}
