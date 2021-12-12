package org.junit.rules;

import org.junit.runners.model.Statement;
import org.junit.runner.Description;

public interface TestRule {
    Statement apply(Statement var1, Description var2);
}
