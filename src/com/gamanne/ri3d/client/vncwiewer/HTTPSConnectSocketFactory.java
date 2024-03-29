package com.gamanne.ri3d.client.vncwiewer;

//  Copyright (C) 2002 Constantin Kaplinsky, Inc.  All Rights Reserved.
//
//  This is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation; either version 2 of the License, or
//  (at your option) any later version.
//
//  This software is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this software; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
//  USA.
//

//
// HTTPSConnectSocketFactory.java together with HTTPSConnectSocket.java
// implement an alternate way to connect to VNC servers via one or two
// HTTPS proxies supporting the HTTP CONNECT method.
//

//
// Based on HTTPConnectSocketFactory.java, adapted to use HTTPS.
// Copyright (C) 2009 Colin Dean. 
//

import java.applet.*;
import java.net.*;
import javax.net.ssl.*;
import java.io.*;

class HTTPSConnectSocketFactory implements SocketFactory {

  public Socket createSocket(String host, int port, Applet applet)
    throws IOException {

    return createSocket(host, port, applet.getParameter("URL"));
  }

  public Socket createSocket(String host, int port, String[] args)
    throws IOException {

    return createSocket(host, port, readArg(args, "URL"));
  }

  public Socket createSocket(String host, int port,
			     String urlString)
    throws IOException {

    if (urlString == null) {
      System.out.println("Incomplete parameter list for HTTPConnectSocket");
      return new Socket(host, port);
    }

    HTTPSConnectSocket s =
	new HTTPSConnectSocket(host, port, urlString);

    return s.getSocket();
  }

  private String readArg(String[] args, String name) {

    for (int i = 0; i < args.length; i += 2) {
      if (args[i].equalsIgnoreCase(name)) {
	try {
	  return args[i+1];
	} catch (Exception e) {
	  return null;
	}
      }
    }
    return null;
  }
}

