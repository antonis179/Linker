package org.amoustakos.linker;

import org.amoustakos.linker.ui.UI;

public class UIStarter {

    public static void init(){
        UI gui = new UI(new JettyServer());
        gui.init();
    }

}
