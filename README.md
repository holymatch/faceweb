# Face Information Server

[![Face Information Server Docker Image](https://dockerbuildbadges.quelltext.eu/status.svg?organization=holymatch&repository=faceweb)](https://hub.docker.com/r/holymatch/faceweb/)

The source code is for my dissertations A Deep Learning Based Face Recognition Application with Augmented Reality on Microsoft Hololens. This server is used to store the person information and request [Face Engine](https://github.com/holymatch/faceengine) to recognize person. 

# Run the server
It is highly recommand to use [face system docker compose](https://github.com/holymatch/facesystem) to run the Face Information Server with the [Face Engine](https://github.com/holymatch/faceengine).

```sh
docker-compose up -d
```
