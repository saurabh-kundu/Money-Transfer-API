package com.moneytransferapp;

import java.sql.SQLException;

import com.moneytransferapp.createTable.service.CreateInitialTableService;
import com.moneytransferapp.createTable.service.impl.CreateInitialTableServiceImpl;
import com.moneytransferapp.factory.FactoryClass;
import com.moneytransferapp.factory.FactoryClassConstants;
import com.moneytransferapp.moneytransfer.exception.CustomException;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.ext.web.Router;

/**
 * Starting point of Application
 */

public class MainApplication extends AbstractVerticle {

	private static RouteApiController routeApiController;
	private static CreateInitialTableService createInitialTableService;

	public MainApplication(RouteApiController routeApiController) {
		MainApplication.routeApiController = routeApiController;
	}

	public MainApplication() {}

	public static void main(String[] args) throws CustomException, SQLException {
		routeApiController = (RouteApiController) FactoryClass.getNewInstance("RouteApiController");	
		createInitialTableService = (CreateInitialTableServiceImpl) FactoryClass.getNewInstance(
				FactoryClassConstants.CREATE_INITIAL_TABLE_SERVICE);
		createInitialTableService.populateInitialData();
		Launcher.executeCommand("run", MainApplication.class.getName());
	}

	@Override
	public void start(Future<Void> fut) throws SQLException, CustomException {
		Router router = Router.router(vertx);

		routeApiController.routingRestApiCalls(router);

		vertx.createHttpServer().requestHandler(router::accept).listen(8080,result -> {
			if (result.succeeded()) {
				fut.complete();
			} else {
				fut.fail(result.cause());
			}});
	}

	public static CreateInitialTableService getCreateInitialTableService() {
		return createInitialTableService;
	}

	public static void setCreateInitialTableService(CreateInitialTableService createInitialTableService) {
		MainApplication.createInitialTableService = createInitialTableService;
	}

	public static RouteApiController getRouteApiController() {
		return routeApiController;
	}

	public static void setRouteApiController(RouteApiController routeApiController) {
		MainApplication.routeApiController = routeApiController;
	}
}