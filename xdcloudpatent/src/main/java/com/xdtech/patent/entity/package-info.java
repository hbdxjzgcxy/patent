/**
 * @author changfei
 *
 */
@GenericGenerators({ @GenericGenerator(name = "uuid", strategy = "uuid"), @GenericGenerator(name = "increment", strategy = "increment"),
		@GenericGenerator(name = "auth_info_seq", strategy = "enhanced-table", parameters = { @Parameter(name = "table_name", value = "ID_SEQUENCE"),
				@Parameter(name = "value_column_name", value = "NEXT"), @Parameter(name = "segment_column_name", value = "SEQ_NAME"),
				@Parameter(name = "segment_value", value = "AUTH_INFO_SEQ"), @Parameter(name = "increment_size", value = "1"),
				@Parameter(name = "optimizer", value = "pooled-lo") }),
		@GenericGenerator(name = "user_seq", strategy = "enhanced-table", parameters = { @Parameter(name = "table_name", value = "ID_SEQUENCE"),
				@Parameter(name = "value_column_name", value = "NEXT"), @Parameter(name = "segment_column_name", value = "SEQ_NAME"),
				@Parameter(name = "segment_value", value = "USER_SEQ"), @Parameter(name = "increment_size", value = "1"),
				@Parameter(name = "optimizer", value = "pooled-lo") }),
		@GenericGenerator(name = "db_folder_seq", strategy = "enhanced-table", parameters = { @Parameter(name = "table_name", value = "ID_SEQUENCE"),
				@Parameter(name = "value_column_name", value = "NEXT"), @Parameter(name = "segment_column_name", value = "SEQ_NAME"),
				@Parameter(name = "segment_value", value = "DB_FOLDER_SEQ"), @Parameter(name = "increment_size", value = "1"),
				@Parameter(name = "optimizer", value = "pooled-lo") }),
		@GenericGenerator(name = "log_seq", strategy = "enhanced-table", parameters = { @Parameter(name = "table_name", value = "ID_SEQUENCE"),
				@Parameter(name = "value_column_name", value = "NEXT"), @Parameter(name = "segment_column_name", value = "SEQ_NAME"),
				@Parameter(name = "segment_value", value = "LOG_SEQ"), @Parameter(name = "increment_size", value = "1"),
				@Parameter(name = "optimizer", value = "pooled-lo") }),
		@GenericGenerator(name = "menu_seq", strategy = "enhanced-table", parameters = { @Parameter(name = "table_name", value = "ID_SEQUENCE"),
				@Parameter(name = "value_column_name", value = "NEXT"), @Parameter(name = "segment_column_name", value = "SEQ_NAME"),
				@Parameter(name = "segment_value", value = "MENU_SEQ"), @Parameter(name = "increment_size", value = "1"),
				@Parameter(name = "optimizer", value = "pooled-lo") }),
		@GenericGenerator(name = "patent_db_seq", strategy = "enhanced-table", parameters = { @Parameter(name = "table_name", value = "ID_SEQUENCE"),
				@Parameter(name = "value_column_name", value = "NEXT"), @Parameter(name = "segment_column_name", value = "SEQ_NAME"),
				@Parameter(name = "segment_value", value = "PATENT_DB_SEQ"), @Parameter(name = "increment_size", value = "1"),
				@Parameter(name = "optimizer", value = "pooled-lo") }),
		@GenericGenerator(name = "role_seq", strategy = "enhanced-table", parameters = { @Parameter(name = "table_name", value = "ID_SEQUENCE"),
				@Parameter(name = "value_column_name", value = "NEXT"), @Parameter(name = "segment_column_name", value = "SEQ_NAME"),
				@Parameter(name = "segment_value", value = "ROLE_SEQ"), @Parameter(name = "increment_size", value = "1"),
				@Parameter(name = "optimizer", value = "pooled-lo") }),
		@GenericGenerator(name = "search_history_seq", strategy = "enhanced-table", parameters = {
				@Parameter(name = "table_name", value = "ID_SEQUENCE"), @Parameter(name = "value_column_name", value = "NEXT"),
				@Parameter(name = "segment_column_name", value = "SEQ_NAME"), @Parameter(name = "segment_value", value = "SEARCH_HISTORY_SEQ"),
				@Parameter(name = "increment_size", value = "1"), @Parameter(name = "optimizer", value = "pooled-lo") }),
		@GenericGenerator(name = "user_favoritor_seq", strategy = "enhanced-table", parameters = {
				@Parameter(name = "table_name", value = "ID_SEQUENCE"), @Parameter(name = "value_column_name", value = "NEXT"),
				@Parameter(name = "segment_column_name", value = "SEQ_NAME"), @Parameter(name = "segment_value", value = "USER_FAVORITOR_SEQ"),
				@Parameter(name = "increment_size", value = "1"), @Parameter(name = "optimizer", value = "pooled-lo") }),
		@GenericGenerator(name = "map_config_seq", strategy = "enhanced-table", parameters = {
				@Parameter(name = "table_name", value = "ID_SEQUENCE"), @Parameter(name = "value_column_name", value = "NEXT"),
				@Parameter(name = "segment_column_name", value = "SEQ_NAME"), @Parameter(name = "segment_value", value = "MAP_CONFIG_SEQ"),
				@Parameter(name = "increment_size", value = "1"), @Parameter(name = "optimizer", value = "pooled-lo") }),
		@GenericGenerator(name = "import_cfg_seq", strategy = "enhanced-table", parameters = {
				@Parameter(name = "table_name", value = "ID_SEQUENCE"), @Parameter(name = "value_column_name", value = "NEXT"),
				@Parameter(name = "segment_column_name", value = "SEQ_NAME"), @Parameter(name = "segment_value", value = "IMPORT_CFG_SEQ"),
				@Parameter(name = "increment_size", value = "1"), @Parameter(name = "optimizer", value = "pooled-lo") }) }

		)

package com.xdtech.patent.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.GenericGenerators;
import org.hibernate.annotations.Parameter;
