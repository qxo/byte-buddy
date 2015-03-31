package net.bytebuddy.instrumentation.method.bytecode.stack.constant;

import net.bytebuddy.instrumentation.Instrumentation;
import net.bytebuddy.instrumentation.method.MethodDescription;
import net.bytebuddy.instrumentation.method.bytecode.stack.StackManipulation;
import net.bytebuddy.test.utility.MockitoRule;
import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.asm.Type;
import org.objectweb.asm.MethodVisitor;

import java.util.Arrays;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class MethodTypeConstantTest {

    private static final String FOO = "foo", BAR = "bar";

    @Rule
    public TestRule mockitoRule = new MockitoRule(this);

    @Mock
    private MethodDescription methodDescription;

    @Mock
    private MethodVisitor methodVisitor;

    @Mock
    private Instrumentation.Context instrumentationContext;

    @Before
    public void setUp() throws Exception {
        when(methodDescription.getDescriptor()).thenReturn(FOO);
    }

    @Test
    public void testApplication() throws Exception {
        StackManipulation stackManipulation = MethodTypeConstant.of(methodDescription);
        assertThat(stackManipulation.isValid(), is(true));
        StackManipulation.Size size = stackManipulation.apply(methodVisitor, instrumentationContext);
        assertThat(size.getSizeImpact(), is(1));
        assertThat(size.getMaximalSize(), is(1));
    }

    @Test
    public void testObjectProperties() throws Exception {
        final Iterator<String> iterator = Arrays.asList(Type.getDescriptor(Void.class), Type.getDescriptor(String.class)).iterator();
        ObjectPropertyAssertion.of(MethodTypeConstant.class).refine(new ObjectPropertyAssertion.Refinement<MethodDescription>() {
            @Override
            public void apply(MethodDescription mock) {
                when(mock.getDescriptor()).thenReturn(iterator.next());
            }
        }).apply();
    }
}
