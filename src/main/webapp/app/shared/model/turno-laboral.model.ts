export interface ITurnoLaboral {
  id?: number;
  codigo?: string;
  nombre?: string;
  horaInicio?: string;
  horaFin?: string;
  descripcion?: string | null;
  activo?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
}

export class TurnoLaboral implements ITurnoLaboral {
  constructor(
    public id?: number,
    public codigo?: string,
    public nombre?: string,
    public horaInicio?: string,
    public horaFin?: string,
    public descripcion?: string | null,
    public activo?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
