export interface IFactorRh {
  id?: number;
  codigo?: string;
  nombre?: string;
  descripcion?: string | null;
  activo?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
}

export class FactorRh implements IFactorRh {
  constructor(
    public id?: number,
    public codigo?: string,
    public nombre?: string,
    public descripcion?: string | null,
    public activo?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
