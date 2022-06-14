package com.cht.xmind;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class XMindParser {

	ObjectMapper jackson;
	
	public XMindParser() {
		jackson = new ObjectMapper();		
	}
	
	public Node parse(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			ZipInputStream zis = new ZipInputStream(fis);
			
			ZipEntry ze;
			while ((ze = zis.getNextEntry()) != null) {
				String name = ze.getName();
				if ("content.json".equals(name)) {
					Reader reader = new InputStreamReader(zis, "UTF-8");
					
					return parse(reader);
				}
			}			
		}
		
		throw new IOException("Illegal file format");
	}
	
	Node parse(Reader reader) throws IOException {
		Map<?, ?>[] sheets = jackson.readValue(reader, Map[].class);
		for (Map<?, ?> sheet : sheets) {
			Map<?, ?> root = (Map<?, ?>) sheet.get("rootTopic");
			
			return trace(root);
		}
		
		return null;
	}
	
	Node trace(Map<?, ?> topic) {
		Node node = new Node();
		node.setTitle((String) topic.get("title"));
		Map<?, ?> children = (Map<?, ?>) topic.get("children");
		if (children != null) {
			List<Map<?, ?>> ts = (List<Map<?, ?>>) children.get("attached");
			for (Map<?, ?> t : ts) {
				Node child = trace(t);
				node.getChildren().add(child);
			}
		}
		
		return node;
	}
}
