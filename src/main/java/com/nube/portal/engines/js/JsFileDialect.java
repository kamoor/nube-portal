//package com.nube.portal.engines.js;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.thymeleaf.dialect.AbstractDialect;
//import org.thymeleaf.dialect.AbstractDialect;
//import org.thymeleaf.processor.IProcessor;
///**
// * Thymelead dialect to include ServerSide JS files
// * @author kamoorr
// *
// */
//@Component
//public class JsFileDialect extends AbstractDialect {
//	
//	@Autowired
//	JsSrcProcessor jsSrcProcessor;
//	 
//    public JsFileDialect() {
//        super();
//    }
// 
//    public String getPrefix() {
//        return "sjs";
//    }
// 
// 
//     @Override
//    public Set<IProcessor> getProcessors() {
//        final Set<IProcessor> processors = new HashSet<IProcessor>();
//        processors.add(jsSrcProcessor);
//        return processors;
//    }
//
//
//
//	@Override
//	public boolean isLenient() {
//		// TODO Auto-generated method stub
//		return false;
//	}
// 
//}