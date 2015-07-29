package kc87.gui;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import java.util.Optional;

@RunWith(JUnitParamsRunner.class)
public class DialogsTest
{

   @SuppressWarnings("unused")
   private Object[] getInvalidTestIds() {
      return new Object[] {
              Optional.empty(),
              Optional.of("a12"),
              Optional.of("-12"),
              Optional.of("12.89"),
              Optional.of("0"),
              Optional.of("255")
      };
   }

   @SuppressWarnings("unused")
   private Object[] getValidTestIds() {
      return new Object[] {
        new Object[]{ Optional.of("1"), "127.0.0.1" },
        new Object[]{ Optional.of("35"), "127.0.0.35" },
        new Object[]{ Optional.of("251"), "127.0.0.251" }
      };
   }

   @Test
   @Parameters(method = "getInvalidTestIds")
   public void getLocalIpFromInvalidIdShouldBeEmpty(Optional<String> id) throws Exception {
      assertThat(Dialogs.getLocalIpFromId(id),equalTo(Optional.empty()));
   }

   @Test
   @Parameters(method = "getValidTestIds")
   public void getLocalIpFromValidIdShouldBeValidIp(Optional<String> id, String ip) throws Exception {
      assertThat(Dialogs.getLocalIpFromId(id).get(),equalTo(ip));
   }
}