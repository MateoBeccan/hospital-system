export interface IPais {
  id?: number;
  nombre?: string;
  codigoIso?: string;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  activo?: boolean;
}

export class Pais implements IPais {
  constructor(
    public id?: number,
    public nombre?: string,
    public codigoIso?: string,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public activo?: boolean,
  ) {
    this.activo = this.activo ?? false;
  }
}
