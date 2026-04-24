export interface IObraSocial {
  id?: number;
  codigo?: string;
  nombre?: string;
  telefono?: string | null;
  email?: string | null;
  direccion?: string | null;
  activo?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
}

export class ObraSocial implements IObraSocial {
  constructor(
    public id?: number,
    public codigo?: string,
    public nombre?: string,
    public telefono?: string | null,
    public email?: string | null,
    public direccion?: string | null,
    public activo?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
