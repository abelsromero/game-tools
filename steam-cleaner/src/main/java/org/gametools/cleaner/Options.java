package org.gametools.cleaner;

import com.beust.jcommander.Parameter;

public final class Options {

    @Parameter(names = {"-a", "--app-id"}, required = true, order = 1)
    String appId;

    @Parameter(names = {"-s", "--storage-drive"}, order = 2)
    boolean storageDrive = false;


}
