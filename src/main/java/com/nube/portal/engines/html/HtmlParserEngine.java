package com.nube.portal.engines.html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.script.ScriptException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.nube.core.exception.NubeException;
import com.nube.portal.engines.html.tags.HtmlTag;
import com.nube.portal.engines.html.tags.HtmlTagInclude;
import com.nube.portal.engines.html.tags.HtmlTagScript;

@Component
public class HtmlParserEngine {
	
	
	static Logger logger = Logger.getLogger(HtmlParserEngine.class);

	
	
	
	public Document parse(String html){
		Document doc = Jsoup.parse(html);
		doc.outputSettings().escapeMode(EscapeMode.xhtml);
		doc.outputSettings().charset("UTF-8");
		return doc;
	}
	
	public Document parse(File file){
		try{
			Document doc = Jsoup.parse(file, null);
			doc.outputSettings().escapeMode(EscapeMode.xhtml);
			doc.outputSettings().charset("UTF-8");
			
			return doc;
		}catch(IOException e){
			logger.error(e);
			return null;
		}
	}
	
	/**
	 * Returns all the custom built tags
	 * @param document
	 * @return
	 */
	public List<HtmlTag> findCustomTags(Document document){
		
		List<HtmlTag> customTags = new ArrayList<HtmlTag>();
		short i=0;
		//Find include tag
		for(Element e: document.select(HtmlTagInclude.instance.tagFinder())){
			customTags.add(new HtmlTagInclude(e.attr(HtmlTagInclude.ATTR_SRC), i++));
		}
		i=0;
		//find script tag
		for(Element e: document.select(HtmlTagScript.instance.tagFinder())){
			HtmlTag tag = new HtmlTagScript(e.attr(HtmlTagScript.ATTR_SRC),i++);
			tag.setContent(e.text());
			customTags.add(tag);
		}
		return customTags;
	}
	
	
	/**
	 * Replace custom built tags with something else
	 * @param document
	 * @return
	 */
	public void replace(Document document, HtmlTag tag, String newContent){
		
		for(Element e: document.select(tag.tagFinder())){
			// logger.info("found" + tag.tagFinder() + "-->"+ "content"+ e.toString());
			if(tag.isHtmlContent()){
				e.html(newContent == null?"":newContent);
			}else{
				e.text(newContent == null?"":newContent);
			}
			break;
		}
		
	}
	
	
	/**
	 * Write document to a file
	 * @param document
	 * @param f
	 */
	public void writeDocument(Document document, File f){
		
		logger.info("Compile time resolution for file: "+ f.getName());
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(f));
			//Unescape inline javascripts and server side javascript;
			writer.write(StringEscapeUtils.unescapeHtml4(document.html()));
			writer.close();
		} catch (IOException e) {
			logger.error("Error writing new content");
		}
		
	}
	
	
	public static void main(String args[]) throws NubeException, ScriptException, IOException{
		
		File f = new File("/Users/kamoorr/git/nube-portal-1.0/nube-portal/src/main/webapp/~core/sjs/xyz/../rest.js");
		System.out.println(f.getParent());
		
		String input = "<div><nube:include src=\"sdad.xml\"/><div>dasdasd{{test1}}<script>print(\"sadasd\");</script></div><div>{{test2}}</div>";
		HtmlParserEngine htmlParserEngine = new HtmlParserEngine();
		
		Document document = htmlParserEngine.parse(input);
		System.out.println(document.toString());
		Elements nubes = document.select("nube|include[src]");
		Elements mustaches = document.getElementsContainingOwnText("{{");
		for(Element e: nubes){
			System.out.println(e.attr("src"));
			
		}
		
		for(Element e: mustaches){
			System.out.println("Mustache \n"+ e.toString());
		}
		
		
		
		
		System.out.println("Done");
	}
}
