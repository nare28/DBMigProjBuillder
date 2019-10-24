package com.dbmigproj.builder;

import com.dbmigproj.builder.beans.MigBean;

public class MigrationRunner {

	public static void main(String[] args) {
		DBMigProjBuilder builder = new DBMigProjBuilderImpl();
		args = new String[] { 
				"/Users/nanarah/AWS_SCT/SCT_Output/Microsoft\\ SQL\\ Server-20190919-17-51-06/",
				"/Users/nanarah/AWS_SCT/SCT_Output/Amazon\\ RDS\\ for\\ PostgreSQL-20190919-16-11-59/",
				"pg",
				"sql"
		};
		MigBean bean = new MigBean();
		builder.initConfig(bean, args);
		builder.setSourceTargetDBs(bean);
		builder.translateSchema(bean);
		builder.splitSchema(bean);
		builder.uploadFilesToRepo(bean);
		builder.applyCleanupRules(bean);
		builder.uploadFilesToRepo(bean);
		builder.applyTranslateRules(bean);
		builder.uploadFilesToRepo(bean);

	}

}
