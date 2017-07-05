package org.amoustakos.linker;

import org.amoustakos.linker.exceptions.ServerException;

public final class CLIStarter {

	public static void init() throws ServerException {
		// Start the server
		new JettyServer().start();
	}

}
