# Identitas
[![CI for Identitas](https://github.com/rickie95/identitas/actions/workflows/maven-publish.yml/badge.svg?branch=main)](https://github.com/rickie95/identitas/actions/workflows/maven-publish.yml)

## A bad copy of OAuth

We're bored so we thought it'd be cool to implement another identity provider which nobody will use.

Anyways, if you really want to host your personal identity provider for your own services go ahead you crazy man.

### How does it work?
Exposes an endpoint where user performs a POST request with user and password. Easy.

Authentication is another POST, which returns a JWT signed with a private key. 

Public key (useful for decrypt and authenticate the JWT) is available on the dedicated endpoint.

### FAQ
 - **Why?**
 - Why not?