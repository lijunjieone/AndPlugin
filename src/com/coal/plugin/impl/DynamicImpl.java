package com.coal.plugin.impl;

public class DynamicImpl implements IDynamic {

    @Override
    public String helloWorld() {
        return "This is content from Plugin!";
    }
}
