//
// Created by AM32160 on 7/8/2016.
//

#ifndef ROBOTIQ_FT_UDPSERVER_H
#define ROBOTIQ_FT_UDPSERVER_H


#include <boost/asio/io_service.hpp>

class SensorUdpServer {

public:
    SensorUdpServer(boost::asio::io_service service);
};


#endif //ROBOTIQ_FT_UDPSERVER_H
