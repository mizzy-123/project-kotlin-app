package com.example.crud

class DataModel{
    var tanggal: String? = null
    var namatoko: String? = null
    var namasales: String? = null
    var kategori: String? = null
    var namabrg: String? = null
    var qty: String? = null

    constructor(tanggal: String?, namatoko: String?, namasales: String?, kategori: String?, namabrg: String?, qty: String?){
        this.tanggal = tanggal
        this.namatoko = namatoko
        this.namasales = namasales
        this.kategori = kategori
        this.namabrg = namabrg
        this.qty = qty
    }
}