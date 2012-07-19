package org.jpos.jposext.isomsgaction.testing.service.support;

import org.jpos.jposext.isomsgaction.testing.annotation.TestIsoMapping;
import org.jpos.jposext.isomsgaction.testing.service.support.ISOMsgActionJunit3;

@TestIsoMapping(mappingsDir="mapping-samples",mappingId="*", interactive=true)
public class TestISOMappings extends ISOMsgActionJunit3 {
}
