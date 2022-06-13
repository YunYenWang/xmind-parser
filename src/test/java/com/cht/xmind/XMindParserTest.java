package com.cht.xmind;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XMindParserTest {

	@Test
	void test() throws Exception {
		var parser = new XMindParser();
		
		var file = new File("C:/users/ricky/OneDrive/文件/xxx.xmind");

		var node = parser.parse(file);
		
		var pw = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"));
		
		dump(pw, node, 0);
		pw.flush();
	}
	
	void dump(PrintWriter pw, Node node, int level) {
		for (int i = 0;i < level;i++) {
			pw.print('\t');
		}
		
		pw.println(node.title);
		for (var child : node.getChildren()) {
			dump(pw, child, level + 1);
		}
	}
}
