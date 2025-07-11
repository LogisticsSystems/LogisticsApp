package com.company.logistics;

import com.company.logistics.core.contracts.Engine;
import com.company.logistics.core.factory.EngineFactory;

public class StartUp {
    public static void main(String[] args) {
        Engine engine = EngineFactory.create();
        engine.start();

    }

}
