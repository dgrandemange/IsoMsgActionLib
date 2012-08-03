package org.jpos.jposext.isomsgaction.testing.service.support;

import org.jpos.jposext.isomsgaction.testing.annotation.TestIsoMapping;
import org.jpos.jposext.isomsgaction.testing.service.support.ISOMsgActionJunit3;

@TestIsoMapping(mappingsDir="src/integrationtest/resources/mapping-config", mappingTestsDir="src/integrationtest/resources/mapping-config-test", mappingId="*", interactive=true)
public class TestISOMappingsSpecifyingTestDir extends ISOMsgActionJunit3 {
}
