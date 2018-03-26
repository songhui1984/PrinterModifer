package com.neusoft.graphene.basecomponent.printer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.neusoft.graphene.basecomponent.printer.tools.Common;


@Repository
public class PrinterDao {
	@Autowired
	JdbcTemplate jdbc;

	/**
	 * 加载所有的模板列表打印展示页面使用
	 * @return
	 */
	public List<TypesetTemplate> loadAllTypesetTemplate() {
		List<TypesetTemplate> res = jdbc.query("select * from t_printer_template", new RowMapper<TypesetTemplate>() {
			@Override
			public TypesetTemplate mapRow(ResultSet tc, int arg1) throws SQLException {
				TypesetTemplate tt = new TypesetTemplate();
				tt.setId(tc.getLong("id"));//id
				tt.setCode(tc.getString("code"));//代码 iyb_proposal
				tt.setName(tc.getString("name"));//名称 iyb建议书1
				tt.setSignId(Common.toLong(tc.getString("sign")));//

				tt.setWorkDir(tc.getString("work_dir"));//模板目录
				tt.setTemplateFile(tc.getString("template_file"));//模板文件
				tt.setTestFile(tc.getString("test_file"));//数据文件

				return tt;
			}
		});

		return res;
	}

	//P12备份文件=CER文件+私钥；所以有了这个p12就再也不用担心证书丢失了。
	//	特点:1、包含私钥、公钥及其证书
	//	2、密钥库和私钥用相同密码进行保护
	public Map<Long, Sign> loadAllSign() {
		final Map<Long, Sign> signs = new HashMap<Long, Sign>();
		//lex_test.p12 Abcd1234 test
		jdbc.query("select * from t_printer_sign", new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Sign s = new Sign();
				s.setId(rs.getLong("id"));
				s.setKeystore(rs.getString("keystore"));
				s.setPassword(rs.getString("password"));
				s.setScope(rs.getString("scope"));
				s.setReason(rs.getString("reason"));
				s.setLocation(rs.getString("location"));

				signs.put(s.getId(), s);
			}

		});

		return signs;
	}

    //'dd2dada7e27f51a5832dc4e7903be28d', '8', 'pdf', '1', '1', '27', '1', '1', '121.41.137.163', '2017-11-07 17:02:49'
	/**
	 * 打印日志
	 * @param client dd2dada7e27f51a5832dc4e7903be28d client_key
	 * @param templateId 模板id
	 * @param fileType 文件类型
	 * @param outType  1/2
	 * @param result 1
	 * @param ip 121.41.137.163
	 * @param time1 
	 * @param time2
	 * @param operateTime
	 * @param pages
	 */
	public void log(String client, Long templateId, String fileType, int outType, int result, String ip, int build_time,
			int export_time, Date operateTime, int pages) {
		String sql = "insert into t_printer_log (client_key, template_id, file_type, out_type, build_time, export_time, page, result, ip, operate_time) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbc.update(sql, client, templateId, fileType, outType, build_time, export_time, pages, result, ip, operateTime);
	}

	//	'1', '736b37e5118b51e2b8f6de00392ad1a7', '众安保险'
	//	'2', 'bc63f3d7f4ab58818283e9ad697cd05f', '豹云科技'
	//	'3', 'dd2dada7e27f51a5832dc4e7903be28d', '本地测试'
	public Map<String, Long> loadAllClient() {
		final Map<String, Long> m = new HashMap<>();

		String sql = "select * from t_printer_client";
		jdbc.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				m.put(rs.getString("key"), rs.getLong("id"));
			}
		});

		return m;
	}

	/**
	 * 
	 * @param code
	 * @param name
	 * @return
	 */
	public TypesetTemplate create(String code, String name) {
		jdbc.update("insert into t_printer_template(code, name) values(?,?)", code, name);

		Long id = jdbc.queryForObject("select last_insert_id()", Long.class);

		TypesetTemplate tt = new TypesetTemplate();
		tt.setId(id);
		tt.setCode(code);
		tt.setName(name);
		tt.setWorkDir(id.toString());

		jdbc.update("update t_printer_template set work_dir = ? where id = ?", tt.getWorkDir(), tt.getId());

		return tt;
	}

	
	public void save(TypesetTemplate tt) {
		jdbc.update(
				"update t_printer_template set code = ?, name = ?, template_file = ?, test_file = ?, sign = ? where id = ?",
				tt.getCode(), tt.getName(), tt.getTemplateFile(), tt.getTestFile(), tt.getSignId(), tt.getId());
	}

}
