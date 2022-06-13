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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XMindParser {

	ObjectMapper jackson;
	
	public XMindParser() {
		jackson = new ObjectMapper();		
	}
	
	public Node parse(File file) throws IOException {
		try (var fis = new FileInputStream(file)) {
			var zis = new ZipInputStream(fis);
			
			ZipEntry ze;
			while ((ze = zis.getNextEntry()) != null) {
				var name = ze.getName();
				if ("content.json".equals(name)) {
					var reader = new InputStreamReader(zis, "UTF-8");
					
					return parse(reader);
				}
			}			
		}
		
		throw new IOException("Illegal file format");
	}
	
	Node parse(Reader reader) throws IOException {
		var sheets = jackson.readValue(reader, Map[].class);
		for (var sheet : sheets) {
			var root = (Map<?, ?>) sheet.get("rootTopic");
			
			var node = trace(root);

			return node;
		}
		
		return null;
	}
	
	Node trace(Map<?, ?> topic) {
		var node = new Node();
		node.setTitle((String) topic.get("title"));
		var children = (Map<?, ?>) topic.get("children");
		if (children != null) {
			var ts = (List<Map<?, ?>>) children.get("attached");
			for (var t : ts) {
				var child = trace(t);
				node.getChildren().add(child);
			}
		}
		
		return node;
	}
}
