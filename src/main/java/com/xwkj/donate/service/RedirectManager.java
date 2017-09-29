package com.xwkj.donate.service;

public interface RedirectManager {

    /**
     * Push a path to redirect cache.
     *
     * @param path
     * @return
     */
    String push(String path);

    /**
     * Pop a path from redirect cahce by state code.
     *
     * @param state
     * @return
     */
    String pop(String state);

}
