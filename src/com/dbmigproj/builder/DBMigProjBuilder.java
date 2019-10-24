package com.dbmigproj.builder;

import com.dbmigproj.builder.beans.MigBean;

public interface DBMigProjBuilder {

	public void initConfig(MigBean bean, String[] args);

	public void setSourceTargetDBs(MigBean bean);

	public void translateSchema(MigBean bean);

	public void splitSchema(MigBean bean);

	public void uploadFilesToRepo(MigBean bean);

	public void applyCleanupRules(MigBean bean);

	public void applyTranslateRules(MigBean bean);

}
