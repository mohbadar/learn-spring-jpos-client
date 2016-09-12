package lab.aikibo.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMUX;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISORequest;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.GenericPackager;

import lab.aikibo.App;

public class ClientImpl implements Client {
  String hostname;
  int portNumber;
  ISOMUX isoMux;
  ASCIIChannel channel;

  private void init() {
    hostname = "localhost";
    portNumber = 10000;

    try {
      ISOPackager packager = new GenericPackager("packager/iso93ascii.xml");
      channel = new ASCIIChannel(hostname, portNumber, packager);
      isoMux = new ISOMUX(channel) {
        @Override
        protected String getKey(ISOMsg m) throws ISOException {
          return super.getKey(m);
        }
      };

      new Thread(isoMux).start();
    } catch(ISOException isoe) {
      App.getLogger().debug("Kesalahan ISO di ClientImpl.init: " + isoe);
    }
  }


  @Override
  public void connect() {
    init();
    try {
      ISOMsg networkReq = new ISOMsg();
      networkReq.setMTI("1800");
      networkReq.set(2, "0280001");
      networkReq.set(3, "123456");
      networkReq.set(7, new SimpleDateFormat("yyyyMMdd").format(new Date()));
      networkReq.set(11, "000001");
      networkReq.set(12, new SimpleDateFormat("HHmmss").format(new Date()));
      networkReq.set(13, new SimpleDateFormat("MMdd").format(new Date()));
      networkReq.set(48, "Tutorial ISO 8583 Dengan Java");
      networkReq.set(70, "001");

      ISORequest req = new ISORequest(networkReq);
      isoMux.queue(req);

      ISOMsg reply = req.getResponse(30*1000);
      if(reply != null) {
        App.getLogger().debug("Req [" + new String(networkReq.pack()) + "]");
        App.getLogger().debug("Res [" + new String(reply.pack()) + "]");
      }
    } catch(ISOException isoe) {
      App.getLogger().debug(isoe);
    }
  }

  @Override
  public void connect2() {
    init();
    try {
      ISOMsg networkReq = new ISOMsg();
      networkReq.setMTI("0800");
      App.getLogger().debug("Isi data: " + networkReq.getValue());
      networkReq.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
      networkReq.set(11, "112233");
      networkReq.set(70, "001");

      for(int i=1; i<=50000; i++) {
        long startTime = System.currentTimeMillis();
        ISORequest req = new ISORequest(networkReq);
        isoMux.queue(req);

        ISOMsg reply = req.getResponse(30*1000);
        if(reply != null) {
          App.getLogger().debug("Req [" + new String(networkReq.pack()) + "]");
          App.getLogger().debug("Res [" + new String(reply.pack()) + "]");
          App.getLogger().debug(reply.getMTI());
          App.getLogger().debug(reply.getString(1));
          App.getLogger().debug(reply.getString(2));
          App.getLogger().debug(reply.getString(7));
          App.getLogger().debug(reply.getString(11));
          App.getLogger().debug(reply.getString(39));
          App.getLogger().debug(reply.getString(70));
        }
        App.getLogger().debug("Iterasi ke " + i + " selesai dalam " + (System.currentTimeMillis() - startTime) + " ms.");
      }
    } catch(ISOException isoe) {
      App.getLogger().debug(isoe);
    }
  }

  @Override
  public void inquiry() {
    App.getLogger().debug("Masuk ke Inquiry");
    init();
    try {
      ISOMsg networkReq = new ISOMsg();
      networkReq.setMTI("0200");
      networkReq.set(2, "028051521");
      networkReq.set(3, "360000");
      networkReq.set(4, "000000000000");
      networkReq.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
      networkReq.set(11, "112255");
      networkReq.set(12, new SimpleDateFormat("HHmmss").format(new Date()));
      networkReq.set(13, new SimpleDateFormat("MMdd").format(new Date()));
      networkReq.set(18, "7012");
      networkReq.set(32, "0028");
      networkReq.set(37, "1");
      networkReq.set(41, "02");
      networkReq.set(48, "0223329090901000000371994");
      networkReq.set(49, "360");
      networkReq.set(63, "00214");

      App.getLogger().debug("Mengirimkan request");
      ISORequest req = new ISORequest(networkReq);
      isoMux.queue(req);

      App.getLogger().debug("Menunggu response dari Server");
      ISOMsg reply = req.getResponse(50*1000);
      App.getLogger().debug("Sampai sini?");
      if(reply != null) {
        App.getLogger().debug("Req [" + new String(networkReq.pack()) + "]");
        App.getLogger().debug("Res [" + new String(reply.pack()) + "]");
        App.getLogger().debug(reply.getMTI());
        App.getLogger().debug("Response Code: " + reply.getString(39));
        App.getLogger().debug("Additional Data: " + reply.getString(48));
        String additionalData = reply.getString(48);
        App.getLogger().debug("NOP : " + additionalData.substring(3,21));
        App.getLogger().debug("THN : " + additionalData.substring(21,25));
        App.getLogger().debug("NAMA WP : " + additionalData.substring(25,55));
        App.getLogger().debug("ALAMAT OP : " + additionalData.substring(55,100));
        App.getLogger().debug("KODE PEMDA : " + additionalData.substring(100,104));
        App.getLogger().debug("POKOK : " + additionalData.substring(104,116));
        App.getLogger().debug("DENDA : " + additionalData.substring(116,128));
      } else {
        App.getLogger().debug("Response dari Server NULL");
      }
      App.getLogger().debug("reply: " + reply);
    } catch(ISOException isoe) {
      App.getLogger().debug(isoe);
    }
  }
}
