CREATE TABLE audittrail_entry(database_id  INTEGER IDENTITY ,process_instance BIGINT NOT NULL ,timestamp VARCHAR,type VARCHAR,workflow_element LONGVARCHAR,originator LONGVARCHAR,data LONGVARCHAR, PRIMARY KEY (database_id) );
CREATE TABLE id(id  INTEGER IDENTITY , PRIMARY KEY (id) );
CREATE TABLE log(message LONGVARCHAR,trace LONGVARCHAR,attributes LONGVARCHAR,host LONGVARCHAR,timestamp TIMESTAMP DEFAULT '' NOT NULL );
CREATE TABLE paragraph(database_id  INTEGER IDENTITY ,xml LONGVARCHAR DEFAULT '' NOT NULL ,  PRIMARY KEY (database_id) );
CREATE TABLE paragraph_mapping(audittrail_entry BIGINT NOT NULL ,paragraph BIGINT NOT NULL ,  PRIMARY KEY (audittrail_entry) );
CREATE TABLE process(database_id  INTEGER IDENTITY ,id LONGVARCHAR DEFAULT '' NOT NULL ,data LONGVARCHAR, PRIMARY KEY (database_id) );
CREATE TABLE xml_log (database_id INTEGER IDENTITY,  `timestamp` timestamp NOT NULL default CURRENT_TIMESTAMP,  `log` longblob NOT NULL,  PRIMARY KEY  (`database_id`));
