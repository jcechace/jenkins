package jenkins.model.identity;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import hudson.ExtensionList;
import hudson.model.UnprotectedRootAction;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import javax.annotation.Nullable;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.TestExtension;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class IdentityRootActionTest {

    @Rule
    public JenkinsRule r = new JenkinsRule();

    @Test
    public void ui() throws Exception {
        HtmlPage p = r.createWebClient().goTo("instance-identity");
        assertThat(p.getElementById("fingerprint").getTextContent(),
                containsString(ExtensionList.lookup(UnprotectedRootAction.class).get(IdentityRootAction.class).getFingerprint()));
    }

    @TestExtension // TODO remove once instance-identity with provider impl embedded in war
    public static class ProviderImpl extends InstanceIdentityProvider<RSAPublicKey,RSAPrivateKey> {

        private final KeyPair keyPair;

        public ProviderImpl() throws NoSuchAlgorithmException, InvalidKeySpecException {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            this.keyPair = generator.generateKeyPair();
        }

        @Nullable
        @Override
        public KeyPair getKeyPair() {
            return keyPair;
        }

        @Nullable
        @Override
        public X509Certificate getCertificate() {
            return null;
        }
    }
}
