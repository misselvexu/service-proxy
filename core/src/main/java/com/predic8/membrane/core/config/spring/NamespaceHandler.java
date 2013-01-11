/* Copyright 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.membrane.core.config.spring;

import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class NamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("router", new RouterParser());
		registerBeanDefinitionParser("proxy", new ProxyParser());
		registerBeanDefinitionParser("serviceProxy", new ServiceProxyParser());
		
		// If you get a compiler error in the following line, enable Annotation Processing
		// in your java compiler using the membrane-esb-annot module as annotation processor factory.
		NamespaceHandlerAutoGenerated.registerBeanDefinitionParsers(this);
		
		try {
			registerBeanDefinitionParser("servletTransport", 
					(BeanDefinitionParser) Class.forName("com.predic8.membrane.servlet.embedded.ServletTransportParser").newInstance());
		} catch (ClassNotFoundException e) {
			// do nothing
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void registerBeanDefinitionParser2(String elementName, BeanDefinitionParser parser) {
		registerBeanDefinitionParser(elementName, parser);
	}
}