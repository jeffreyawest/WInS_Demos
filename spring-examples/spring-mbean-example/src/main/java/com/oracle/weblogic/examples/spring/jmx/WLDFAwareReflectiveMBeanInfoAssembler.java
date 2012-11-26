package com.oracle.weblogic.demo.spring.jmx;

import javax.management.Descriptor;

import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;
import org.springframework.jmx.export.assembler.SimpleReflectiveMBeanInfoAssembler;

/**
 * Created with IntelliJ IDEA.
 * User: jeffrey.a.west
 * Date: 4/3/12
 * Time: 2:39 PM
 */
public class WLDFAwareReflectiveMBeanInfoAssembler
    extends SimpleReflectiveMBeanInfoAssembler
{
  private static final String WLDF_MBEAN_TYPE_DESCPTR_KEY = "DiagnosticTypeName";
  private static final String NAME_MBEAN_DESCPTR_KEY = "name";
  private static final String MBEAN_KEYNAME_SUFFIX = "MBean";

  @Override
  protected void populateMBeanDescriptor(Descriptor descriptor, Object managedBean, String beanKey)
  {
    super.populateMBeanDescriptor(descriptor, managedBean, beanKey);
    descriptor.setField(WLDF_MBEAN_TYPE_DESCPTR_KEY, descriptor.getFieldValue(NAME_MBEAN_DESCPTR_KEY) + MBEAN_KEYNAME_SUFFIX);
  }
}