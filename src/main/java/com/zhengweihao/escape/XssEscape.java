package com.zhengweihao.escape;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class XssEscape {

	private XssEscape() {
		super();
	}
	
	public static void main(String[] args) {
		File htmlTextFile = new File("D:\\tmp\\html_text.html");
		FileReader reader = null;
		BufferedReader bufferedReader = null;
		StringBuffer sb = new StringBuffer();
		try {
			reader = new FileReader(htmlTextFile);
			bufferedReader = new BufferedReader(reader);
			String readLine = null;
			while ((readLine = bufferedReader.readLine()) != null) {
				if (sb.toString().length() > 0) {
					sb.append(System.lineSeparator());
				}
				sb.append(readLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		long st = System.currentTimeMillis();
		String result = xssFilter(sb.toString());
		long et = System.currentTimeMillis();
		System.out.println("XSS过滤完毕,耗时:" + (et - st) + "(ms)");

		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(htmlTextFile);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("文件重新写入完毕");
	}

	/**
	 * 过滤html中的xss
	 * 过滤内容:
	 * 	<script>标签(转移<符号)
	 * 	标签中的on*=(事件)字符javascript:(脚本)字符
	 */
	public static String xssFilter(String html) {
		if (html == null || html.length() <= 0) {
			return html;
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < html.length(); i++) {
			char c = html.charAt(i);
			int tagLength = i + 7;
			if (c == '<' && tagLength <= html.length()) {// 标签处理
				String tag = html.substring(i, tagLength);
				if (tag.toLowerCase().equals("<script")) {// script标签，转义标签字符
					sb.append("&lt;");

					int scriptHtmlLength = 0;
					String scriptHtml = html.substring(i + 1, html.length());
					for (int j = 0; j < scriptHtml.length(); j++) {
						char lc = scriptHtml.charAt(j);
						int endTagLength = j + 9;
						if (lc == '<' && endTagLength <= scriptHtml.length()) {
							String endTag = scriptHtml.substring(j, endTagLength);
							if (endTag.toLowerCase().equals("</script>")) {
								scriptHtml = scriptHtml.substring(0, j) + "&lt;"
										+ scriptHtml.substring(j + 1, endTagLength);
								sb.append(scriptHtml);
								scriptHtmlLength = j + 9;
								break;
							}
						}
					}

					i += scriptHtmlLength;
					continue;
				} else {// 非script标签，过滤标签中的事件及javascript代码
					String tagHtml = html.substring(i, html.length());
					tagHtml = tagHtml.substring(0, tagHtml.indexOf(">") + 1);
					int tagHtmlLenght = tagHtml.length();
					tagHtml = tagHtml.replaceAll("on[a-zA-Z]*=", "filterEvent=").replaceAll("javascript:",
							"filterJavascript:");
					sb.append(tagHtml);
					i += tagHtmlLenght - 1;
					continue;
				}
			}
			sb.append(c);
		}

		return sb.toString();
	}
}
