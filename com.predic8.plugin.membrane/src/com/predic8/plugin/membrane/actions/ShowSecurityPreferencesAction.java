/* Copyright 2009 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.plugin.membrane.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.widgets.Display;

import com.predic8.plugin.membrane.preferences.SecurityPreferencePage;

public class ShowSecurityPreferencesAction extends Action {

	public static final String ID = "Show Security Preferences Page Action";
	

	public ShowSecurityPreferencesAction() {
		setText("Show Security Preferences");
		setId(ID);
	}
	
	@Override
	public void run() {
		
		IPreferencePage page = new SecurityPreferencePage() ;
		PreferenceManager mgr = new PreferenceManager();
		IPreferenceNode node = new PreferenceNode(SecurityPreferencePage.PAGE_ID, page);
		mgr.addToRoot(node);
		PreferenceDialog dialog = new PreferenceDialog(Display.getCurrent().getActiveShell(), mgr);
		dialog.create();
		dialog.setMessage(page.getTitle());
		dialog.open();
		
//		PreferenceManager manager = MembraneUIPlugin.getDefault().getWorkbench().getPreferenceManager();
//		PreferenceDialog dlg = new PreferenceDialog(Display.getCurrent().getActiveShell(), manager);
//		dlg.setSelectedNode(SecurityPreferencePage.PAGE_ID);	
//		dlg.open();
	}
	
}
