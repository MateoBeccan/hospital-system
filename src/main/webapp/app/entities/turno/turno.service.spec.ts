import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_FORMAT, DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Turno } from '@/shared/model/turno.model';

import TurnoService from './turno.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Turno Service', () => {
    let service: TurnoService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new TurnoService();
      currentDate = new Date();
      elemDefault = new Turno(123, 'AAAAAAA', currentDate, 0, 'AAAAAAA', 'AAAAAAA', currentDate, false, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          fechaHora: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaCreacion: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Turno', async () => {
        const returnedFromService = {
          id: 123,
          fechaHora: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaCreacion: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = {
          fechaHora: currentDate,
          fechaCreacion: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Turno', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Turno', async () => {
        const returnedFromService = {
          codigo: 'BBBBBB',
          fechaHora: dayjs(currentDate).format(DATE_TIME_FORMAT),
          duracionMinutos: 1,
          motivoConsulta: 'BBBBBB',
          observaciones: 'BBBBBB',
          fechaCreacion: dayjs(currentDate).format(DATE_TIME_FORMAT),
          activo: true,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };

        const expected = {
          fechaHora: currentDate,
          fechaCreacion: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Turno', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Turno', async () => {
        const patchObject = {
          codigo: 'BBBBBB',
          fechaHora: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaCreacion: dayjs(currentDate).format(DATE_TIME_FORMAT),
          activo: true,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          ...new Turno(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = {
          fechaHora: currentDate,
          fechaCreacion: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Turno', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Turno', async () => {
        const returnedFromService = {
          codigo: 'BBBBBB',
          fechaHora: dayjs(currentDate).format(DATE_TIME_FORMAT),
          duracionMinutos: 1,
          motivoConsulta: 'BBBBBB',
          observaciones: 'BBBBBB',
          fechaCreacion: dayjs(currentDate).format(DATE_TIME_FORMAT),
          activo: true,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = {
          fechaHora: currentDate,
          fechaCreacion: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Turno', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Turno', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Turno', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
