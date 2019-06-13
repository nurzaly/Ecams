package my.gov.ilpsdk.apps.ecams.model;

import java.io.Serializable;

public class Assets implements Serializable{
	private Integer id;
	private String no_siri_pendaftaran;
	private String barcode;
	private String kategori;
	private String sub_kategori;
	private String jenis;
	private String jenis_aset;
	private String no_casis;
	private String kod_lokasi;
	private Locations lokasi;
	private String pegawai;
	private Integer verification_id;

	public Assets(Integer id, String no_siri_pendaftaran, String barcode, String kategori, String sub_kategori, String jenis, String jenis_aset, String no_casis, String kod_lokasi, Locations lokasi, String pegawai, Integer verification_id) {
		this.id = id;
		this.no_siri_pendaftaran = no_siri_pendaftaran;
		this.barcode = barcode;
		this.kategori = kategori;
		this.sub_kategori = sub_kategori;
		this.jenis = jenis;
		this.jenis_aset = jenis_aset;
		this.no_casis = no_casis;
		this.kod_lokasi = kod_lokasi;
		this.lokasi = lokasi;
		this.pegawai = pegawai;
		this.verification_id = verification_id;
	}

	public Integer getId() {
		return id;
	}

	public Integer getVerification_id() {
		return verification_id;
	}

	public String getNo_siri_pendaftaran() {
		return no_siri_pendaftaran;
	}

	public String getBarcode() {
		return barcode;
	}

	public String getKategori() {
		return kategori;
	}

	public String getSub_kategori() {
		return sub_kategori;
	}

	public String getJenis() {
		return jenis;
	}

	public String getJenis_aset() {
		return jenis_aset;
	}

	public String getNo_casis() {
		return no_casis;
	}

	public String getKod_lokasi() {
		return kod_lokasi;
	}

	public Locations getLokasi() {
		return lokasi;
	}

	public String getPegawai() {
		return pegawai;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setVerification_id(Integer verification_id) {
		this.verification_id = verification_id;
	}

	public void setNo_siri_pendaftaran(String no_siri_pendaftaran) {
		this.no_siri_pendaftaran = no_siri_pendaftaran;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public void setKategori(String kategori) {
		this.kategori = kategori;
	}

	public void setSub_kategori(String sub_kategori) {
		this.sub_kategori = sub_kategori;
	}

	public void setJenis(String jenis) {
		this.jenis = jenis;
	}

	public void setJenis_aset(String jenis_aset) {
		this.jenis_aset = jenis_aset;
	}

	public void setNo_casis(String no_casis) {
		this.no_casis = no_casis;
	}

	public void setKod_lokasi(String kod_lokasi) {
		this.kod_lokasi = kod_lokasi;
	}

	public void setLokasi(Locations lokasi) {
		this.lokasi = lokasi;
	}

	public void setPegawai(String pegawai) {
		this.pegawai = pegawai;
	}

}

