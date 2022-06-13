package com.cht.xmind;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Node {
	
	String title;
	
	List<Node> children = new ArrayList<>();
}
