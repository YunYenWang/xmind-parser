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
		XMindParser parser = new XMindParser();
		
		File file = new File("C:/users/ricky/OneDrive/文件/xxx.xmind");

		Node node = parser.parse(file);
		
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"));
		
		dump(pw, node, 0);
		pw.flush();
	}
	
	void dump(PrintWriter pw, Node node, int level) {
		for (int i = 0;i < level;i++) {
			pw.print('\t');
		}
		
		pw.println(node.title);
		for (Node child : node.getChildren()) {
			dump(pw, child, level + 1);
		}
	}
}
