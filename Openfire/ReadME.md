ʹ�÷�����
��XMPPServer���RMI���ɣ�
try { 
	SendMessageService rhello = new SendMessageImpl("Start Server!"); 
    LocateRegistry.createRegistry(RmiFlag.OPENFIRE_JAVAEE_PORT); 
    Naming.bind(RmiFlag.OPENFIRE_JAVAEE_NAME, rhello); 
} catch (RemoteException e) { 
	e.printStackTrace(); 
} catch (AlreadyBoundException e) { 
    e.printStackTrace(); 
} catch (MalformedURLException e) { 
    e.printStackTrace(); 
} 