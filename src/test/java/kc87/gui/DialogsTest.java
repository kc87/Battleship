package kc87.gui;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import java.util.Optional;

public class DialogsTest
{
   @Test
   public void testGetLocalIpFromId() throws Exception
   {
      Optional<String> id = Optional.empty();
      assertThat(Dialogs.getLocalIpFromId(id),equalTo(Optional.empty()));

      id = Optional.of("a12");
      assertThat(Dialogs.getLocalIpFromId(id),equalTo(Optional.empty()));

      id = Optional.of("-12");
      assertThat(Dialogs.getLocalIpFromId(id),equalTo(Optional.empty()));

      id = Optional.of("12.89");
      assertThat(Dialogs.getLocalIpFromId(id),equalTo(Optional.empty()));

      id = Optional.of("0");
      assertThat(Dialogs.getLocalIpFromId(id),equalTo(Optional.empty()));

      id = Optional.of("255");
      assertThat(Dialogs.getLocalIpFromId(id),equalTo(Optional.empty()));

      id = Optional.of("201");
      assertThat(Dialogs.getLocalIpFromId(id).get(),equalTo("127.0.0.201"));
   }

}