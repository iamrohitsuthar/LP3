
| Alice                                | Bob                                  |
|--------------------------------------|--------------------------------------|
| Decides value of P and G             | Decides value of P and G             |
| Alice select his private key  = a             | Bob select his private key = b             |
| Alice public key generated: x = G<sup>a</sup> mod P        | Bob public key generated: y = G<sup>b</sup> mod P        |
| Key received = y                     | key received = x                     |
| Generated Secret Key: ka = y<sup>a</sup> mod P | Generated Secret Key: kb = x<sup>b</sup> mod P |

ka == kb so now both alice and bob shares same secreat key

Reference: https://www.geeksforgeeks.org/implementation-diffie-hellman-algorithm/
