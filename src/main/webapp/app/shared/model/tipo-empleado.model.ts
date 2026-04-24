export interface ITipoEmpleado {
  id?: number;
  codigo?: string;
  nombre?: string;
  descripcion?: string | null;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  activo?: boolean;
}

export class TipoEmpleado implements ITipoEmpleado {
  constructor(
    public id?: number,
    public codigo?: string,
    public nombre?: string,
    public descripcion?: string | null,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public activo?: boolean,
  ) {
    this.activo = this.activo ?? false;
  }
}
