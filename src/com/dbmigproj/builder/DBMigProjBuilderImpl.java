package com.dbmigproj.builder;

import com.dbmigproj.builder.beans.ConfigBean;
import com.dbmigproj.builder.beans.MigBean;
import com.dbmigproj.builder.tools.ProcedureSplitter;

public class DBMigProjBuilderImpl implements DBMigProjBuilder {

	@Override
	public void initConfig(MigBean bean, String[] args) {
		ConfigBean config = new ConfigBean();
		config.setSrcDir(args[0]);
		config.setTgtDir(args[1]);
		config.setSrcType(args[2]);
		config.setTgtType(args[3]);
		config.setSrcProcFile("create-routine.sql");
		config.setTgtProcFile("create-procedure.sql");
		System.out.println(config.toString());
		bean.setConfig(config);
	}

	@Override
	public void setSourceTargetDBs(MigBean bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void translateSchema(MigBean bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void splitSchema(MigBean bean) {
		ProcedureSplitter spl = new ProcedureSplitter();
		spl.split();
		
	}

	@Override
	public void uploadFilesToRepo(MigBean bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyCleanupRules(MigBean bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyTranslateRules(MigBean bean) {
		// TODO Auto-generated method stub
		
	}


}
