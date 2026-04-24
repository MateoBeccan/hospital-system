export interface ITipoDocumento {
  id?: number;
  codigo?: string;
  nombre?: string;
  sigla?: string;
  descripcion?: string | null;
  activo?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
}

export class TipoDocumento implements ITipoDocumento {
  constructor(
    public id?: number,
    public codigo?: string,
    public nombre?: string,
    public sigla?: string,
    public descripcion?: string | null,
    public activo?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
